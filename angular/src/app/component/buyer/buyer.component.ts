import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
@Component({
  selector: 'app-buyer',
  templateUrl: './buyer.component.html',
  styleUrls: ['./buyer.component.css']
})
export class BuyerComponent implements OnInit {

  searchForm!: FormGroup;
  constructor(private fb: FormBuilder,
              private router: Router) { }


  ngOnInit(): void {
    this.searchForm = this.fb.group({
      search: this.fb.control('', [Validators.required])

    })
  }

  onSubmit(){
    let search = this.searchForm.value;
    this.router.navigate(['buyer/search', search.search])
  }





}
