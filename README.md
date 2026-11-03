# Spring Boot microservices

## Start the user service dependencies

Compose reads `.env` from the repository root, next to `docker-compose.yml`.
From the repository root in PowerShell:

```powershell
Copy-Item .env.example .env
```

Edit `.env` and set a strong `POSTGRES_PASSWORD`. Generate a Base64 JWT key
containing at least 32 random bytes, then set it as `JWT_SECRET`:

```powershell
$rng = [Security.Cryptography.RandomNumberGenerator]::Create()
$bytes = New-Object byte[] 32
$rng.GetBytes($bytes)
[Convert]::ToBase64String($bytes)
```

Start the stack from the repository root:

```powershell
docker compose up --build -d
docker compose ps
```

The service is available at `http://localhost:8081`. PostgreSQL and Kafka are
also published only on localhost. PostgreSQL is available on host port `5433`
to avoid conflicting with a local PostgreSQL instance; the application
container uses the internal Compose addresses for both.

When running the service directly on the host, set `POSTGRES_PASSWORD` and
`JWT_SECRET` in the process environment; the datasource defaults to
`localhost:5433` and Kafka to `localhost:9092`. Compose supplies the container
addresses and credentials automatically.

If PostgreSQL reports password authentication failures after changing
`POSTGRES_PASSWORD`, the existing `postgres_data` volume may still have the
old database password. Once the Postgres container is running, update the role
password to match the root `.env` value without removing the volume:

```powershell
$password = (Get-Content .env | Where-Object { $_ -match '^POSTGRES_PASSWORD=' } | Select-Object -First 1) -replace '^POSTGRES_PASSWORD=', ''
$sqlPassword = $password.Replace("'", "''")
"ALTER ROLE postgres WITH PASSWORD '$sqlPassword';" | docker compose exec -T postgres psql -U postgres -d ecommerce
Remove-Variable password, sqlPassword
docker compose up --build -d
```
