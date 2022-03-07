import { HttpClient } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { lastValueFrom } from "rxjs";
import { Item } from "../models/item.model";

@Injectable()
export class LazadaItemsService{

  constructor(private http: HttpClient){
  }

  productId!: number;
  shopeeItems!: Item[];

  // INCOMPLETE
  public async getListings(){
    let listing = await lastValueFrom(this.http.get('api/lazada/listings')).then(r=>{
      console.log(r);
    });
    console.log(listing);
  }

}
