import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IClient } from 'app/shared/model/client.model';
import { getEntities as getClients } from 'app/entities/client/client.reducer';
import { IAgent } from 'app/shared/model/agent.model';
import { getEntities as getAgents } from 'app/entities/agent/agent.reducer';
import { ITicket } from 'app/shared/model/ticket.model';
import { TicketStatus } from 'app/shared/model/enumerations/ticket-status.model';
import { getEntity, updateEntity, createEntity, reset } from './ticket.reducer';

export const TicketUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const clients = useAppSelector(state => state.client.entities);
  const agents = useAppSelector(state => state.agent.entities);
  const ticketEntity = useAppSelector(state => state.ticket.entity);
  const loading = useAppSelector(state => state.ticket.loading);
  const updating = useAppSelector(state => state.ticket.updating);
  const updateSuccess = useAppSelector(state => state.ticket.updateSuccess);
  const ticketStatusValues = Object.keys(TicketStatus);

  const handleClose = () => {
    navigate('/ticket' + location.search);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getClients({}));
    dispatch(getAgents({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    values.issuedAt = convertDateTimeToServer(values.issuedAt);

    const entity = {
      ...ticketEntity,
      ...values,
      issuedBy: clients.find(it => it.id.toString() === values.issuedBy.toString()),
      assignedTo: agents.find(it => it.id.toString() === values.assignedTo.toString()),
    };

    if (isNew) {
      dispatch(createEntity(entity));
    } else {
      dispatch(updateEntity(entity));
    }
  };

  const defaultValues = () =>
    isNew
      ? {
          issuedAt: displayDefaultDateTime(),
        }
      : {
          status: 'RECEIVED',
          ...ticketEntity,
          issuedAt: convertDateTimeFromServer(ticketEntity.issuedAt),
          issuedBy: ticketEntity?.issuedBy?.id,
          assignedTo: ticketEntity?.assignedTo?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="ticketsManagementApp.ticket.home.createOrEditLabel" data-cy="TicketCreateUpdateHeading">
            <Translate contentKey="ticketsManagementApp.ticket.home.createOrEditLabel">Create or edit a Ticket</Translate>
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? (
                <ValidatedField
                  name="id"
                  required
                  readOnly
                  id="ticket-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('ticketsManagementApp.ticket.code')}
                id="ticket-code"
                name="code"
                data-cy="code"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('ticketsManagementApp.ticket.status')}
                id="ticket-status"
                name="status"
                data-cy="status"
                type="select"
              >
                {ticketStatusValues.map(ticketStatus => (
                  <option value={ticketStatus} key={ticketStatus}>
                    {translate('ticketsManagementApp.TicketStatus.' + ticketStatus)}
                  </option>
                ))}
              </ValidatedField>
              <ValidatedField
                label={translate('ticketsManagementApp.ticket.issueDescription')}
                id="ticket-issueDescription"
                name="issueDescription"
                data-cy="issueDescription"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('ticketsManagementApp.ticket.issuedAt')}
                id="ticket-issuedAt"
                name="issuedAt"
                data-cy="issuedAt"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                id="ticket-issuedBy"
                name="issuedBy"
                data-cy="issuedBy"
                label={translate('ticketsManagementApp.ticket.issuedBy')}
                type="select"
              >
                <option value="" key="0" />
                {clients
                  ? clients.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField
                id="ticket-assignedTo"
                name="assignedTo"
                data-cy="assignedTo"
                label={translate('ticketsManagementApp.ticket.assignedTo')}
                type="select"
              >
                <option value="" key="0" />
                {agents
                  ? agents.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/ticket" replace color="info">
                <FontAwesomeIcon icon="arrow-left" />
                &nbsp;
                <span className="d-none d-md-inline">
                  <Translate contentKey="entity.action.back">Back</Translate>
                </span>
              </Button>
              &nbsp;
              <Button color="primary" id="save-entity" data-cy="entityCreateSaveButton" type="submit" disabled={updating}>
                <FontAwesomeIcon icon="save" />
                &nbsp;
                <Translate contentKey="entity.action.save">Save</Translate>
              </Button>
            </ValidatedForm>
          )}
        </Col>
      </Row>
    </div>
  );
};

export default TicketUpdate;
