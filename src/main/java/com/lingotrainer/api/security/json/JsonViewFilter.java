package com.lingotrainer.api.security.json;

import com.lingotrainer.domain.model.user.Role;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.AbstractMappingJacksonResponseBodyAdvice;

import java.util.Collection;

/**
 * Jackson only accepts *.class in the annotation @JsonView.
 * So we need to create a mapper to map a enum to a empty interface class.0
 */
@ControllerAdvice
class JsonViewFilter extends AbstractMappingJacksonResponseBodyAdvice {

    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        return super.supports(returnType, converterType);
    }

    @Override
    protected void beforeBodyWriteInternal(MappingJacksonValue bodyContainer, MediaType contentType,
                                           MethodParameter returnType, ServerHttpRequest request,
                                           ServerHttpResponse response) {

        Class<?> viewClass = MyJsonView.Anonymous.class;

        if (SecurityContextHolder.getContext().getAuthentication() != null
                && SecurityContextHolder.getContext().getAuthentication().getAuthorities() != null) {
            Collection<? extends GrantedAuthority> authorities =
                    SecurityContextHolder.getContext().getAuthentication().getAuthorities();

            if (authorities.stream().anyMatch(o -> o.getAuthority().equals(Role.TRAINEE.getValue()))) {
                viewClass = MyJsonView.Trainee.class;
            }
            if (authorities.stream().anyMatch(o -> o.getAuthority().equals(Role.ADMIN.getValue()))) {
                viewClass = MyJsonView.Admin.class;
            }
        }
        bodyContainer.setSerializationView(viewClass);
    }
}