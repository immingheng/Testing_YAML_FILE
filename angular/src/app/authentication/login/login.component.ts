import { HttpClient } from '@angular/common/http';
import { Component, OnInit, Output } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { faKey, faUser } from '@fortawesome/free-solid-svg-icons';
import { lastValueFrom } from 'rxjs';
import { Login } from 'src/app/models/login.model';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {

  @Output()
  isLoggedIn!: boolean;

  loginForm!: FormGroup;
  loginDetails!: Login;

  constructor(private fb: FormBuilder,
              private http: HttpClient,
              private router: Router){}


  ngOnInit(): void {
    this.loginForm = this.fb.group({
      username: this.fb.control('', [Validators.required]),
      password: this.fb.control('', [Validators.required])
    })
  }
  // adding key and user icons
  faKey = faKey;
  faUser = faUser;

  public login(){
    this.loginDetails = this.loginForm.value;
    // POST TO SERVER TO CHECK IF USER EXISTS
    lastValueFrom(this.http.post<any>('api/auth/login',JSON.stringify(this.loginDetails)))
    .then(resolve => {
      console.log(resolve);
    })
    .catch(reject =>{
      console.error(reject);
    })
    this.router.navigate(['/seller']);
    this.isLoggedIn = true;
    console.log(this.isLoggedIn);
  }

}
