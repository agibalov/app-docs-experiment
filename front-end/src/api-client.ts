import {Injectable} from "@angular/core";
import {Http, Response} from "@angular/http";
import {NoteWithIdDto} from "./note-with-id-dto";
import {NoteDto} from "./note-dto";

@Injectable()
export class ApiClient {
    constructor(private http: Http) {
    }

    async getNotes(): Promise<NoteWithIdDto[]> {
        const response: Response = await this.http.get('http://localhost:9090/api/notes').toPromise();
        const body: NoteWithIdDto[] = response.json();
        return body;
    }

    async getNote(noteId: number): Promise<NoteWithIdDto> {
        const response: Response = await this.http.get(`http://localhost:9090/api/notes/${noteId}`).toPromise();
        const body: NoteWithIdDto = response.json();
        return body;
    }

    async createNote(noteDto: NoteDto): Promise<NoteWithIdDto> {
        const createNoteResponse: Response = await this.http.post('http://localhost:9090/api/notes', noteDto).toPromise();
        const createdNoteLocation: string = createNoteResponse.headers.get('location');

        const getCreatedNoteResponse = await this.http.get(createdNoteLocation).toPromise();
        const createdNoteDto: NoteWithIdDto = getCreatedNoteResponse.json();

        return createdNoteDto;
    }

    async updateNote(noteId: number, noteDto: NoteDto): Promise<NoteWithIdDto> {
        const noteUrl: string = `http://localhost:9090/api/notes/${noteId}`;
        const updateNoteResponse: Response = await this.http.put(noteUrl, noteDto).toPromise();

        const getUpdatedNoteResponse = await this.http.get(noteUrl).toPromise();
        const updatedNoteDto: NoteWithIdDto = getUpdatedNoteResponse.json();

        return updatedNoteDto;
    }

    async deleteNote(noteId: number): Promise<void> {
        const noteUrl = `http://localhost:9090/api/notes/${noteId}`;
        const response: Response = await this.http.delete(noteUrl).toPromise();
    }
}
