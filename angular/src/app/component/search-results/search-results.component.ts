import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { ScrapeResult } from 'src/app/models/scrapResult.model';

@Component({
  selector: 'app-search-results',
  templateUrl: './search-results.component.html',
  styleUrls: ['./search-results.component.css']
})
export class SearchResultsComponent implements OnInit {

  query!: string;
  noQueryResults: boolean = true;
  results!: ScrapeResult[];

  constructor(private activatedRoute: ActivatedRoute) { }

  ngOnInit(): void {

      this.query = this.activatedRoute.snapshot.params['query'];


      // this.query = lastValueFrom(this.http.post<String>('api/buyer/search', JSON.stringify(this.searchResult)));

    }

}

