package com.hesabla.invoicing.exception;

import java.time.Instant;
import java.util.Map;

/**
 * Bütün xəta cavabları üçün VAHİD JSON forması. Əvvəllər hər xəta sadə
 * mətn (String) kimi qaytarılırdı — front-end kodu adətən hər cavabı
 * response.json() ilə parse etdiyi üçün bu, xəta zamanı frontend-in
 * özündə JSON parse xətasına səbəb olurdu. `fieldErrors` yalnız
 * validasiya xətalarında dolur (sahə adı → xəta mesajı) ki, front-end
 * formda düz sahənin altında düz mesajı göstərə bilsin.
 */
public record ErrorResponse(
        Instant timestamp,
        int status,
        String error,
        String message,
        String path,
        Map<String, String> fieldErrors
) {
}
