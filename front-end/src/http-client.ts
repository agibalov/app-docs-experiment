import {Http, Request, RequestOptionsArgs, Response} from "@angular/http";
import {Injectable} from "@angular/core";
import {Wove} from "aspect.js-angular";
import {TransactionRecorder} from "./transaction-recorder";
import {TransactionEvent} from "./transaction-event";

/**
 * TODO: document this
 */
@Wove()
@Injectable()
export class HttpClient {
    constructor(
        private http: Http,
        private transactionRecorder: TransactionRecorder) {
    }

    async request(url: string|Request, options?: RequestOptionsArgs): Promise<Response> {
        const response: Response = await this.http.request(url, options).toPromise();
        this._notifyTransactionRecorder(response);
        return response;
    }

    async get(url: string, options?: RequestOptionsArgs): Promise<Response> {
        const response: Response = await this.http.get(url, options).toPromise();
        this._notifyTransactionRecorder(response);
        return response;
    }

    async post(url: string, body: any, options?: RequestOptionsArgs): Promise<Response> {
        const response: Response = await this.http.post(url, body, options).toPromise();
        this._notifyTransactionRecorder(response);
        return response;
    }

    async put(url: string, body: any, options?: RequestOptionsArgs): Promise<Response> {
        const response: Response = await this.http.put(url, body, options).toPromise();
        this._notifyTransactionRecorder(response);
        return response;
    }

    async delete(url: string, options?: RequestOptionsArgs): Promise<Response> {
        const response: Response = await this.http.delete(url, options).toPromise();
        this._notifyTransactionRecorder(response);
        return response;
    }

    async patch(url: string, body: any, options?: RequestOptionsArgs): Promise<Response> {
        const response: Response = await this.http.patch(url, options).toPromise();
        this._notifyTransactionRecorder(response);
        return response;
    }

    async head(url: string, options?: RequestOptionsArgs): Promise<Response> {
        const response: Response = await this.http.head(url, options).toPromise();
        this._notifyTransactionRecorder(response);
        return response;
    }

    async options(url: string, options?: RequestOptionsArgs): Promise<Response> {
        const response: Response = await this.http.options(url, options).toPromise();
        this._notifyTransactionRecorder(response);
        return response;
    }

    private _notifyTransactionRecorder(response: Response): void {
        const transactionEventsJson = response.headers.get("X-BackEnd-Transaction");
        if(transactionEventsJson == null) {
            return;
        }

        const transactionEvents: TransactionEvent[] = <TransactionEvent[]>JSON.parse(transactionEventsJson);
        this.transactionRecorder.appendEvents(transactionEvents);
    }
}
