import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { faEnvelope, faKey, faUser } from '@fortawesome/free-solid-svg-icons';
import { Registration } from 'src/app/models/registration.model';

@Component({
  selector: 'app-signup',
  templateUrl: './signup.component.html',
  styleUrls: ['./signup.component.css']
})
export class SignupComponent implements OnInit {

  registrationForm! : FormGroup;
  registrationDetails! : Registration;
  // adding key and user icons
  faKey = faKey;
  faUser = faUser;
  faEnvelope = faEnvelope;


  constructor(private fb: FormBuilder) { }

  ngOnInit(): void {
    this.registrationForm = this.fb.group({
      username : this.fb.control('',Validators.required),
      name : this.fb.control('',Validators.required),
      email : this.fb.control('',Validators.email),
      password : this.fb.control('',Validators.required)}
    )

  }

    public Register(){

    }

}
