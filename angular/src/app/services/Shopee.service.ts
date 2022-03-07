import { HttpClient, HttpParams } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { lastValueFrom } from "rxjs";
import { Item } from "../models/item.model";
import { Listing } from "../models/listing.model";

@Injectable()
export class ShopeeItemsService{

  constructor(private http: HttpClient){
  }

  items!:Item[];
  item_ids: number[] = [];
  data!: Listing;
  public getListings(shop_id: string): Promise<Item[]> {
    const params = new HttpParams().set('shop_id', shop_id);

    // DEVELOPMENT
    // return lastValueFrom(this.http.get<Item[]>('http://localhost:8080/api/shopee/listings', {params}))


    // PRODUCTION
    return lastValueFrom(this.http.get<Item[]>('https://my-cute-shop.herokuapp.com/api/shopee/listings', {params}))
  };



}
