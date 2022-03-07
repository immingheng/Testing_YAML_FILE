import { HttpClient, HttpParams } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { DomSanitizer, SafeResourceUrl } from '@angular/platform-browser';
import { ActivatedRoute, Router } from '@angular/router';
import { lastValueFrom } from 'rxjs';
import { Item } from 'src/app/models/item.model';
import { LazadaItemsService } from 'src/app/services/Lazada.service';
import { ShopeeItemsService } from 'src/app/services/Shopee.service';

@Component({
  selector: 'app-seller',
  templateUrl: './seller.component.html',
  styleUrls: ['./seller.component.css']
})
export class SellerComponent implements OnInit {

  constructor(private shopeeSvc: ShopeeItemsService,
              private lazadaSvc: LazadaItemsService,
              private activatedRoute: ActivatedRoute,
              private domSanitizer: DomSanitizer,
              private http: HttpClient) { }

  shop_id: any;
  shopee_id: any;
  items!: Item[];
  shopeeItems!: Item[];
  lazadaItems!: Item[];
  imageUrls: SafeResourceUrl[] = [];
  imageUrl!: SafeResourceUrl;

  // Both lazada and shopee are not linked at first
  shopeeLinked: boolean = false;
  shopeeExists!: boolean;
  lazadaLinked: boolean = false;


  async ngOnInit() {

    // check if user exists on db and extract shopee_shop_id by making a call to the backend - number = exists, 0 = doesn't exists
    // PRODUCTION
    await lastValueFrom(this.http.get<Number>('https://my-cute-shop.herokuapp.com/api/auth/user/shopee_shop_id')).then(async r=>{
      // DEVELOPMENT
      // await lastValueFrom(this.http.get<Number>('http://localhost:8080/api/auth/user/shopee_shop_id')).then(async r=>{
      this.shopee_id = r;
      console.log("From DB -> " +this.shopee_id);
      if (this.shopee_id!=0){
        // IF BACKEND RETURNS A SHOPEE_ID, ACCOUNT EXISTS W EXISTING SHOPEE_SHOP_ID
        this.shopeeExists = true;
        // SET SHOP_ID as that returned from backend
        this.shop_id=this.shopee_id;
        console.log("this.shop_id is "+this.shop_id);
        this.shopeeLinked = true;

        // Make a call to another endpoint to retrieve the items within the database to populate in the table.
        const params = new HttpParams().set('shopee_shop_id', this.shop_id);
        console.log("params --> "+params);
        // PRODUCTION
        await lastValueFrom(this.http.get<Item[]>('https://my-cute-shop.herokuapp.com/api/auth/user/shopeeItems',{params})).then(result => {

        // DEVELOPMENT
          // await lastValueFrom(this.http.get<Item[]>('http://localhost:8080/api/auth/user/shopeeItems',{params})).then(result => {
          console.log("Items are populated based on items in database!");
          this.imageUrls = [];
          result.forEach(item =>{
            let itemImage = item.image.replace('"','');
            itemImage = itemImage.replace('"','');
            this.imageUrl = this.domSanitizer.bypassSecurityTrustUrl(itemImage);
            this.imageUrls.push(this.imageUrl);
          })
          this.shopeeItems = result;
          console.log(this.shopeeItems);
        })

      } else {
        // shopee_id = 0 (Account not linked with a shopee id) therefore shopee doesn't exists in db
        this.shopeeExists = false;
        console.log("SHOPEE ID = 0 therefore SHOPEE IS NOT LINKED")
        // THIS IS RUN WHEN CLIENT LINK THEIR ACCOUNT TO GET SHOPEE_ID FROM URL AFTER LINKING IT
        this.shop_id = await this.activatedRoute.snapshot.queryParams['shop_id'];
        console.log(this.shop_id);
        if (this.shop_id!=null){
          this.shopeeLinked=true;
          this.shopeeExists = true;
        }
      }
      }).catch(err=>{
        console.log('SHOPEE SHOP IS NOT LINKED TO THIS USER');
        console.log(err);
      }

    );

  }



  public getShopeeListings(){
    // CALL SHOPEESVC and getListing, passing in shop_id
    this.shopeeSvc.getListings(this.shop_id).then(r=>{
      this.shopeeItems = r;
      console.log("Items are populated using Shopee API call!");
      this.imageUrls = [];
      r.forEach(item=>{
        let itemImage = item.image.replace('"','');
        itemImage = itemImage.replace('"','');
        // console.log(itemImage.toString());
        // ANGULAR BY DEFAULT WILL WANT TO SANITISE THIS TO PROTECT ONE FROM XSS SCRIPTING THEREFORE HAVE TO SANITISE IMAGE URL WITH DOMSANITIZER FOR IMAGES TO BE DISPLAYED
        this.imageUrl = this.domSanitizer.bypassSecurityTrustUrl(itemImage);
        this.imageUrls.push(this.imageUrl);
      })
      // console.log(this.imageUrls);
    })
    .catch(err=>{
      alert('Something went wrong!');
      console.log(err);
    })
  }


  public getLazadaListings(){
    //TODO
  }



  public unlinkShop(){
    //TO BE IMPLEMENTED
    this.shop_id = null;
  }


}
