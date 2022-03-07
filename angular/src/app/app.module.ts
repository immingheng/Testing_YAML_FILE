// Angular
import { NgModule } from '@angular/core';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { BrowserModule } from '@angular/platform-browser';
import { RouterModule, Routes, CanActivate } from '@angular/router';
import { HttpClientModule, HTTP_INTERCEPTORS } from '@angular/common/http'

// Auth0
import { AuthModule, AuthHttpInterceptor, AuthGuard } from '@auth0/auth0-angular';


// FontAwesome-5
import { FontAwesomeModule, FaIconLibrary } from '@fortawesome/angular-fontawesome';
import { faKey, faUser, faEnvelope } from '@fortawesome/free-solid-svg-icons'

// Created Components/Services
import { AppComponent } from './app.component';
import { HomeComponent } from './component/home/home.component';
import { AboutComponent } from './component/about/about.component';
import { ContactComponent } from './component/contact/contact.component';
// LOGIN and SIGNUP HAS BEEN DELEGATED TO AUTH0 FOR JWT VERIFICATION PURPOSES USING THEIR UNIVERSAL LOGIN
// import { LoginComponent } from './authentication/login/login.component';
// import { SignupComponent } from './authentication/signup/signup.component';
import { BuyerComponent } from './component/buyer/buyer.component';
import { SellerComponent } from './component/seller/seller.component';
import { AddNewItemComponent } from './component/add-new-item/add-new-item.component';
import { ForgetPasswordComponent } from './authentication/forget-password/forget-password.component';
import { ShopeeItemsService } from './services/Shopee.service';
import { LazadaItemsService } from './services/Lazada.service';
import { SearchResultsComponent } from './component/search-results/search-results.component';



// Apply canActivate: [SellerGuardService] to prevent unauthorised access to Seller & addItem.
const appRoutes : Routes = [
  {path:"", component:HomeComponent},
  {path:"about", component:AboutComponent},
  {path:"contact", component:ContactComponent},
  // {path:"login", component:LoginComponent},
  // {path:"signup", component:SignupComponent},
  {path:"buyer", component:BuyerComponent},
  {path:"buyer/search/:query", component:SearchResultsComponent},
  {path:"seller", component:SellerComponent, canActivate: [AuthGuard]},
  {path:"seller/addItem", component:AddNewItemComponent, canActivate:[AuthGuard]},
  {path:"forgetPassword", component:ForgetPasswordComponent},
  {path:'**',redirectTo:'/', pathMatch:'full'}
]

@NgModule({
  declarations: [
    AppComponent,
    HomeComponent,
    AboutComponent,
    ContactComponent,
    // LoginComponent,
    // SignupComponent,
    BuyerComponent,
    SellerComponent,
    AddNewItemComponent,
    ForgetPasswordComponent,
    SearchResultsComponent
  ],
  imports: [
    BrowserModule,
    FormsModule, ReactiveFormsModule,
    HttpClientModule,
    RouterModule.forRoot(appRoutes),
    FontAwesomeModule,
    AuthModule.forRoot({
      // Domain and client of Auth0 SPA-App
      domain:'dev-r01a92vl.us.auth0.com',
      clientId:'KhnVRfaSBikVDALT74nTsEGNpkatLvCq',

      // Specify configuration for interceptor
      // Interceptor will intercept any request to the specified uri to check if token exists and handle it accordingly.
      // THEREFORE ANY CALL TO THE SERVER WITH ROUTE /API/* SHOULD BE ADDED WITH THE INTERCEPTORS
      audience: 'https://my-cute-shop.herokuapp.com/api',
      scope: 'read:current_user',


      httpInterceptor: {
        allowedList:[
          {
            // Match any request that during development
            uri:'http://localhost:8080/*',
            tokenOptions: {
              // The attached token should target this audience
              audience: 'https://my-cute-shop.herokuapp.com/api',
              scope: 'read:current_user'

            }
          },
          {
            // Match any request during production
            uri: 'https://my-cute-shop.herokuapp.com/*',
            tokenOptions:{
                // The attached token should target this audience
                audience: 'https://my-cute-shop.herokuapp.com/api',
                scope: 'read:current_user'
            }
          }
        ]
      }

    })
    ],
  providers: [ShopeeItemsService, LazadaItemsService,
    // Adding Auth0 HTTP Interceptor to the providers
    {provide: HTTP_INTERCEPTORS, useClass: AuthHttpInterceptor, multi:true}],

  bootstrap: [AppComponent]
})

export class AppModule {
  constructor(library: FaIconLibrary){
    library.addIcons(faKey);
    library.addIcons(faUser);
    library.addIcons(faEnvelope);
  }
}
