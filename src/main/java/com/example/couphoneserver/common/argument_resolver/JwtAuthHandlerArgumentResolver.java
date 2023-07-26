package com.example.couphoneserver.common.argument_resolver;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@Slf4j
@Component
public class JwtAuthHandlerArgumentResolver implements HandlerMethodArgumentResolver {

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        log.info("[JwtAuthHandlerArgumentResolver.supportsParameter]");
        boolean hasAnnotation = parameter.hasParameterAnnotation(PreAuthorize.class);
        boolean hasType = long.class.isAssignableFrom(parameter.getParameterType());
        return hasAnnotation && hasType;
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {
        log.info("[JwtAuthHandlerArgumentResolver.resolveArgument]");
        HttpServletRequest request = (HttpServletRequest) webRequest.getNativeRequest();
        //request에 userId Attribute를 반환
        return request.getAttribute("userId");
    }

}