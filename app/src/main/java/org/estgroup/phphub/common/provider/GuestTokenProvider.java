package org.estgroup.phphub.common.provider;

import com.github.pwittchen.prefser.library.Prefser;
import static org.estgroup.phphub.common.Constant.*;

public class GuestTokenProvider implements TokenProvider {
    private Prefser prefser;

    public GuestTokenProvider(Prefser prefser) {
        this.prefser = prefser;
    }

    @Override
    public String getToken() {
        if (prefser != null) {
            return prefser.get(GUEST_TOKEN_KEY, String.class, "");
        }
        return null;
    }
}