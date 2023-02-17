import { ITicket } from 'app/shared/model/ticket.model';

export interface IMessage {
  id?: number;
  content?: string;
  ticket?: ITicket | null;
}

export const defaultValue: Readonly<IMessage> = {};
