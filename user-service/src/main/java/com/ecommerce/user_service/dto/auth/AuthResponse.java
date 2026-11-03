
package com.ecommerce.user_service.dto.auth;

public record AuthResponse(
        String accessToken,
        String tokenType
) {
}
