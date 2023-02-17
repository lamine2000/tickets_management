import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './ticket.reducer';

export const TicketDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const ticketEntity = useAppSelector(state => state.ticket.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="ticketDetailsHeading">
          <Translate contentKey="ticketsManagementApp.ticket.detail.title">Ticket</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{ticketEntity.id}</dd>
          <dt>
            <span id="code">
              <Translate contentKey="ticketsManagementApp.ticket.code">Code</Translate>
            </span>
          </dt>
          <dd>{ticketEntity.code}</dd>
          <dt>
            <span id="status">
              <Translate contentKey="ticketsManagementApp.ticket.status">Status</Translate>
            </span>
          </dt>
          <dd>{ticketEntity.status}</dd>
          <dt>
            <span id="issueDescription">
              <Translate contentKey="ticketsManagementApp.ticket.issueDescription">Issue Description</Translate>
            </span>
          </dt>
          <dd>{ticketEntity.issueDescription}</dd>
          <dt>
            <span id="issuedAt">
              <Translate contentKey="ticketsManagementApp.ticket.issuedAt">Issued At</Translate>
            </span>
          </dt>
          <dd>{ticketEntity.issuedAt ? <TextFormat value={ticketEntity.issuedAt} type="date" format={APP_DATE_FORMAT} /> : null}</dd>
          <dt>
            <Translate contentKey="ticketsManagementApp.ticket.issuedBy">Issued By</Translate>
          </dt>
          <dd>{ticketEntity.issuedBy ? ticketEntity.issuedBy.id : ''}</dd>
          <dt>
            <Translate contentKey="ticketsManagementApp.ticket.assignedTo">Assigned To</Translate>
          </dt>
          <dd>{ticketEntity.assignedTo ? ticketEntity.assignedTo.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/ticket" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/ticket/${ticketEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default TicketDetail;
