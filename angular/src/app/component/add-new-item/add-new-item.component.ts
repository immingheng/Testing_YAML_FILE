import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';

@Component({
  selector: 'app-add-new-item',
  templateUrl: './add-new-item.component.html',
  styleUrls: ['./add-new-item.component.css']
})
export class AddNewItemComponent implements OnInit {

  addNewItemForm!: FormGroup;
  categories!: String[];

  constructor(private fb: FormBuilder) { }

  ngOnInit(): void {
    this.addNewItemForm = this.fb.group({

    })
  }

  public onSubmit(){

  }

}
