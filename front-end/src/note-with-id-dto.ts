import {NoteDto} from "./note-dto";

export interface NoteWithIdDto extends NoteDto {
    id: number;
}
