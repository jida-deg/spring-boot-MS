
package com.ecommerce.user_service.event;

public record UserLoginEvent(
        Long userId,
        String name,
        String email
) {
}
