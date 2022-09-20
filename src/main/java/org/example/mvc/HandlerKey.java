package org.example.mvc;

import org.example.mvc.controller.RequestMethod;

import java.util.Objects;

public class HandlerKey {
    private final String uriPath;
    private final RequestMethod requestMethod;

    public HandlerKey(RequestMethod requestMethod, String uriPath) {
        this.requestMethod = requestMethod;
        this.uriPath = uriPath;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        HandlerKey that = (HandlerKey) o;
        return Objects.equals(uriPath, that.uriPath) && requestMethod == that.requestMethod;
    }

    @Override
    public int hashCode() {
        return Objects.hash(uriPath, requestMethod);
    }
}
