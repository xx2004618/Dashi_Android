package com.laioffer.laiofferproject;

/**
 * Created by xiangxiao on 2016/9/6.
 */

import org.scribe.builder.api.DefaultApi10a;
import org.scribe.model.Token;

/**
 * Generic service provider for two-step OAuth10a.
 */
public class TwoStepOAuth extends DefaultApi10a {

    @Override
    public String getAccessTokenEndpoint() { return null; }

    @Override
    public String getAuthorizationUrl(Token unused) {
        return null;
    }

    @Override
    public String getRequestTokenEndpoint() {
        return null;
    }
}

