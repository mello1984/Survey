package ru.butakov.survey.exceptions;

public class SurveyDaoException extends RuntimeException {
    public SurveyDaoException(String message) {
        super(message);
    }

    public SurveyDaoException(String message, Throwable cause) {
        super(message, cause);
    }

    public SurveyDaoException(Throwable cause) {
        super(cause);
    }

    public SurveyDaoException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
