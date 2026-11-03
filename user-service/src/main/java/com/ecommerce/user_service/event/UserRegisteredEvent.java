
package com.ecommerce.user_service.event;

public record UserRegisteredEvent(
        Long userId,
        String name,
        String email
) {
}
