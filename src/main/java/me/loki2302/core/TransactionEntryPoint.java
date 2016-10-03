package me.loki2302.core;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * @undocumented
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface TransactionEntryPoint {
    String value();
}
