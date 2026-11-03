
package com.ecommerce.user_service.event;

public record UserUpdatedEvent(
        Long userId,
        String name,
        String email
) {
}
