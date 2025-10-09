package com.pepo.notasapi.Security.JWT;

import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;

/**
 * Wrapper que permite modificar headers da requisição
 * Usado para substituir o JWT expirado pelo novo JWT após renovação automática
 */
public class HeaderModifierHttpServletRequest extends HttpServletRequestWrapper {

    private final Map<String, String> customHeaders;

    public HeaderModifierHttpServletRequest(HttpServletRequest request, String newAuthorizationHeader) {
        super(request);
        this.customHeaders = new HashMap<>();
        this.customHeaders.put("Authorization", newAuthorizationHeader);
    }

    @Override
    public String getHeader(String name) {
        // Se for um header customizado, retorna o valor customizado
        if (customHeaders.containsKey(name)) {
            return customHeaders.get(name);
        }
        // Caso contrário, retorna o valor original
        return super.getHeader(name);
    }

    @Override
    public Enumeration<String> getHeaderNames() {
        // Combina headers originais com customizados
        List<String> names = Collections.list(super.getHeaderNames());
        names.addAll(customHeaders.keySet());
        return Collections.enumeration(names);
    }

    @Override
    public Enumeration<String> getHeaders(String name) {
        // Se for um header customizado, retorna apenas o valor customizado
        if (customHeaders.containsKey(name)) {
            return Collections.enumeration(Collections.singletonList(customHeaders.get(name)));
        }
        // Caso contrário, retorna os valores originais
        return super.getHeaders(name);
    }
}