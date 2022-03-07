import { DOCUMENT } from '@angular/common';
import { Component, Inject, Input, OnInit } from '@angular/core';
import { AuthService } from '@auth0/auth0-angular';


@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent implements OnInit{

  // FOR DATE IN FOOTER
  title = 'angular';
  d = new Date();
  year = this.d.getFullYear();


  ngOnInit(): void {
  }

  constructor(@Inject(DOCUMENT) public document: Document, public auth: AuthService){
  }






}
