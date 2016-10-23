import {Injectable} from "@angular/core";
import {Http, Response} from "@angular/http";
import {NoteWithIdDto} from "./note-with-id-dto";
import {NoteDto} from "./note-dto";

/**
 * Notepad service API client.
 */
@Injectable()
export class ApiClient {
    constructor(private http: Http) {
    }

    /**
     * Retrieve a list of all notes.
     * @returns {NoteWithIdDto[]} a collection of notes
     */
    async getNotes(): Promise<NoteWithIdDto[]> {
        const response: Response = await this.http.get('/api/notes').toPromise();
        const body: NoteWithIdDto[] = response.json();
        return body;
    }

    /**
     * Get one note by ID.
     * @param noteId an ID of note to retrieve
     * @returns {NoteWithIdDto} a retrieved note
     */
    async getNote(noteId: number): Promise<NoteWithIdDto> {
        const response: Response = await this.http.get(`/api/notes/${noteId}`).toPromise();
        const body: NoteWithIdDto = response.json();
        return body;
    }

    /**
     * Create a note.
     * @param noteDto an object containing all mandatory note attributes
     * @returns {NoteWithIdDto} a created note
     */
    async createNote(noteDto: NoteDto): Promise<NoteWithIdDto> {
        const createNoteResponse: Response = await this.http.post('/api/notes', noteDto).toPromise();
        const createdNoteLocation: string = createNoteResponse.headers.get('location');

        const getCreatedNoteResponse = await this.http.get(createdNoteLocation).toPromise();
        const createdNoteDto: NoteWithIdDto = getCreatedNoteResponse.json();

        return createdNoteDto;
    }

    /**
     * Update an existing note.
     * @param noteId an ID of note to update
     * @param noteDto an object containing updated note attributes
     * @returns {NoteWithIdDto} an updated note
     */
    async updateNote(noteId: number, noteDto: NoteDto): Promise<NoteWithIdDto> {
        const noteUrl: string = `/api/notes/${noteId}`;
        const updateNoteResponse: Response = await this.http.put(noteUrl, noteDto).toPromise();

        const getUpdatedNoteResponse = await this.http.get(noteUrl).toPromise();
        const updatedNoteDto: NoteWithIdDto = getUpdatedNoteResponse.json();

        return updatedNoteDto;
    }

    /**
     * Delete an existing note.
     * @param noteId an ID of note to delete
     */
    async deleteNote(noteId: number): Promise<void> {
        const noteUrl = `/api/notes/${noteId}`;
        const response: Response = await this.http.delete(noteUrl).toPromise();
    }
}
