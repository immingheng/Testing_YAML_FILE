export interface ListingItem{
  item_id: number;
  shopid: number;
  update_time: number;
  status: String;
  item_sku: String;
  variation: String[];
  is_2tier_item: boolean;
  tenure: String[];
}
