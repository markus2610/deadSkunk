package com.r0adkll.deadskunk.network;

import org.apache.http.auth.AuthScheme;
import org.apache.http.auth.AuthSchemeFactory;
import org.apache.http.impl.auth.NTLMScheme;
import org.apache.http.params.HttpParams;

/**
 * Created by drew.heavner on 10/17/13.
 */
public class NTLMSchemeFactory implements AuthSchemeFactory
{
    public AuthScheme newInstance(HttpParams params)
    {
        return new NTLMScheme(new JCIFSEngine());
    }
}
