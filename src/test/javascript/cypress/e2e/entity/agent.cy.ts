import {
  entityTableSelector,
  entityDetailsButtonSelector,
  entityDetailsBackButtonSelector,
  entityCreateButtonSelector,
  entityCreateSaveButtonSelector,
  entityCreateCancelButtonSelector,
  entityEditButtonSelector,
  entityDeleteButtonSelector,
  entityConfirmDeleteButtonSelector,
} from '../../support/entity';

describe('Agent e2e test', () => {
  const agentPageUrl = '/agent';
  const agentPageUrlPattern = new RegExp('/agent(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const agentSample = { firstName: 'Kieran', lastName: 'Emmerich', email: 'Christ.McCullough49@hotmail.com' };

  let agent;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/agents+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/agents').as('postEntityRequest');
    cy.intercept('DELETE', '/api/agents/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (agent) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/agents/${agent.id}`,
      }).then(() => {
        agent = undefined;
      });
    }
  });

  it('Agents menu should load Agents page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('agent');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('Agent').should('exist');
    cy.url().should('match', agentPageUrlPattern);
  });

  describe('Agent page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(agentPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create Agent page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/agent/new$'));
        cy.getEntityCreateUpdateHeading('Agent');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', agentPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/agents',
          body: agentSample,
        }).then(({ body }) => {
          agent = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/agents+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/agents?page=0&size=20>; rel="last",<http://localhost/api/agents?page=0&size=20>; rel="first"',
              },
              body: [agent],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(agentPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details Agent page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('agent');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', agentPageUrlPattern);
      });

      it('edit button click should load edit Agent page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Agent');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', agentPageUrlPattern);
      });

      it('edit button click should load edit Agent page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Agent');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', agentPageUrlPattern);
      });

      it('last delete button click should delete instance of Agent', () => {
        cy.intercept('GET', '/api/agents/*').as('dialogDeleteRequest');
        cy.get(entityDeleteButtonSelector).last().click();
        cy.wait('@dialogDeleteRequest');
        cy.getEntityDeleteDialogHeading('agent').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', agentPageUrlPattern);

        agent = undefined;
      });
    });
  });

  describe('new Agent page', () => {
    beforeEach(() => {
      cy.visit(`${agentPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('Agent');
    });

    it('should create an instance of Agent', () => {
      cy.get(`[data-cy="firstName"]`).type('Alayna').should('have.value', 'Alayna');

      cy.get(`[data-cy="lastName"]`).type('Romaguera').should('have.value', 'Romaguera');

      cy.get(`[data-cy="email"]`).type('Richard.Gutmann@gmail.com').should('have.value', 'Richard.Gutmann@gmail.com');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(201);
        agent = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(200);
      });
      cy.url().should('match', agentPageUrlPattern);
    });
  });
});
