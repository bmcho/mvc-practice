package org.example.view;

import static org.example.view.RedirectView.DEFAULT_REDIRECT_PREFIX;

public class JspViewResolve implements ViewResolver {
    @Override
    public View resolveView(String viewName) {
        if (viewName.startsWith(DEFAULT_REDIRECT_PREFIX)) {
            return new RedirectView(viewName);
        }
        return new JspView(viewName + ".jsp");
    }
}
