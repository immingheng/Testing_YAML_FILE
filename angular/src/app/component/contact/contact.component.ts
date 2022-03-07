import { HttpClient } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { lastValueFrom } from 'rxjs';

@Component({
  selector: 'app-contact',
  templateUrl: './contact.component.html',
  styleUrls: ['./contact.component.css']
})
export class ContactComponent implements OnInit {

  emailForm!: FormGroup;

  constructor(private fb: FormBuilder,
              private http: HttpClient,
              private router: Router) { }

  ngOnInit(): void {
    this.emailForm = this.fb.group({
      subject: this.fb.control('',[Validators.required]),
      message: this.fb.control('',[Validators.required])
    })

  }

  // POST THE FORM TO BACKEND FOR EMAIL TO BE SENT USING SPRINGBOOT EMAIL
  public async post2SB(){
    const email = this.emailForm.value;

    // DEVELOPMENT
    // const send2SB = await lastValueFrom(this.http.post<any>('http://localhost:8080/api/email',JSON.stringify(email),{headers:{'Content-Type':'application/json'}})).then(()=>{
    //   alert('Your email has been sent!')})
    //   .catch(err=>{
    //     alert('Opps! Something went wrong')
    //     console.log(err);
    //   });

    // PRODUCTION
    const send2SB = await lastValueFrom(this.http.post<any>('https://my-cute-shop.herokuapp.com/api/email',JSON.stringify(email),{headers:{'Content-Type':'application/json'}})).then(()=>{
      alert('Your email has been sent!')})
      .catch(err=>{
        alert('Opps, something went wrong!')
        console.log(err);
      });

    this.emailForm.reset();
    this.router.navigate(['/']);

  }

}
