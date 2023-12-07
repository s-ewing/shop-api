package com.example.shopapi.services.impl;

import com.example.shopapi.dto.OrderItemDTO;
import com.example.shopapi.services.StripeService;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class StripeServiceImpl implements StripeService {
    private final Environment env;

    public StripeServiceImpl(Environment env) {
        this.env = env;
    }

    public Session createCheckoutSession(List<OrderItemDTO> items) throws StripeException {
        Stripe.apiKey = env.getProperty("stripe.secret.key");

        SessionCreateParams.Builder builder =
                SessionCreateParams.builder()
                        .setMode(SessionCreateParams.Mode.PAYMENT)
                        .setUiMode(SessionCreateParams.UiMode.EMBEDDED)
                        .setReturnUrl(env.getProperty("stripe.redirect.url"));
        for(OrderItemDTO item : items) {
            SessionCreateParams.LineItem stripeLineItem = SessionCreateParams.LineItem.builder()
                    .setQuantity(Long.valueOf(item.getQuantity()))
                    .setPriceData(SessionCreateParams.LineItem.PriceData.builder()
                            .setCurrency("USD")
                            .setUnitAmount(item.getProduct().getPrice().multiply(BigDecimal.valueOf(100)).longValue()) //convert cents to dollars
                            .setProductData(
                                    SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                            .setName(item.getProduct().getName())
                                            .build())
                            .build())
                    .build();
            builder.addLineItem(stripeLineItem);
        }

        SessionCreateParams params = builder.build();
        return Session.create(params);
    }

    public String getSessionStatus(String sessionId) throws StripeException {
        Session session = Session.retrieve(sessionId);
        return session.getStatus();
    }
}
