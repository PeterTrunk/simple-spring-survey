CREATE TABLE app_user (
                          id          BIGSERIAL PRIMARY KEY,
                          keycloak_id VARCHAR(64)  NOT NULL UNIQUE,
                          username    VARCHAR(100) NOT NULL,
                          created_at  TIMESTAMPTZ  NOT NULL DEFAULT now()
);

CREATE TABLE survey (
                        id                BIGSERIAL PRIMARY KEY,
                        owner_id          BIGINT       NOT NULL REFERENCES app_user (id),
                        share_code        VARCHAR(16)  NOT NULL UNIQUE,
                        password_hash     VARCHAR(100),
                        title             VARCHAR(200) NOT NULL,
                        description       TEXT,
                        thank_you_message TEXT,
                        structure         JSONB        NOT NULL,
                        version           INT          NOT NULL DEFAULT 1,
                        enabled           BOOLEAN      NOT NULL DEFAULT TRUE,
                        anonymous         BOOLEAN      NOT NULL DEFAULT FALSE,
                        expires_at        TIMESTAMPTZ,
                        max_responses     INT,
                        created_at        TIMESTAMPTZ  NOT NULL DEFAULT now(),
                        updated_at        TIMESTAMPTZ  NOT NULL DEFAULT now(),
                        CONSTRAINT chk_max_responses CHECK (max_responses IS NULL OR max_responses > 0)
);

CREATE INDEX idx_survey_owner ON survey (owner_id);

CREATE TABLE survey_response (
                                 id                 BIGSERIAL PRIMARY KEY,
                                 survey_id          BIGINT      NOT NULL REFERENCES survey (id) ON DELETE CASCADE,
                                 respondent_id      BIGINT      NOT NULL REFERENCES app_user (id),
                                 survey_version     INT         NOT NULL,
                                 structure_snapshot JSONB       NOT NULL,
                                 answers            JSONB       NOT NULL,
                                 submitted_at       TIMESTAMPTZ NOT NULL DEFAULT now(),
                                 updated_at         TIMESTAMPTZ NOT NULL DEFAULT now(),
                                 CONSTRAINT uq_response_survey_respondent UNIQUE (survey_id, respondent_id)
);

CREATE INDEX idx_response_respondent ON survey_response (respondent_id);