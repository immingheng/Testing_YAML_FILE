import { ListingItem } from "./listingItem.model";

export interface Listing{
  items: ListingItem[];
  request_id: String;
  more:boolean;
  total:number;
}
