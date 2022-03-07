import { Injectable } from "@angular/core";
import { AuthConfig, OAuthService } from "angular-oauth2-oidc";



@Injectable({
    providedIn: 'root'
})
export class AuthService {

    authCodeFlowConfig: AuthConfig = {
        // URL of identity provider. https://<YOUR_DOMAIN>.auth0.com
        issuer: 'https://dev-r01a92vl.us.auth0.com/',
        redirectUri: window.location.origin,
        clientId: 'KhnVRfaSBikVDALT74nTsEGNpkatLvCq',
        responseType: 'code',
        scope: 'openid profile admin',
        showDebugInformation: true,
        silentRefreshRedirectUri: window.location.origin,
        useSilentRefresh: true,
        customQueryParams: {
            /**
             * replace with your API-Audience
             * This is very important to retrieve a valid access_token for our API
             * */
            audience: 'https://my-cute-shop.herokuapp.com/api',
        },
    };

    constructor(private oauth: OAuthService) {
        this.oauth.configure(this.authCodeFlowConfig);
        this.oauth.loadDiscoveryDocumentAndTryLogin();
        this.oauth.setupAutomaticSilentRefresh();
    }

    login(): void {
        this.oauth.initLoginFlow();
    }
}
