package com.example.hotelbooking.constants;

public class ErrorConstant {
    public static final String INVALID_NAME = "Invalid Name: Name cannot be null.";
    public static final String INVALID_TYPE = "Invalid Type: Must be one of the following [DELUXE, LUXURY, SUITE].";
    public static final String INVALID_DATE = "Invalid date: Please input dates in 'yyyy-MM-dd' format.";
    public static final String INVALID_DATE_ORDER = "Invalid date: Start date must be before end date.";
    public static final String INVALID_RESERVATION_DATES = "The check in and/or check out dates are outside the available dates.";
    public static final String INVALID_DATE_OVERLAP = "The entered date overlaps with an already registered reservation.";
    public static final String INVALID_DATE_NULL_VALUES = "Invalid Date: If a date is entered, both the 'startDate' and the 'endDate' must be provided.";
    public static final String EMPTY_HOTEL_DATES = "Invalid Hotel: A reservation can not be made as the user specified hotel does not have available dates.";
    public static final String INVALID_DATE_CHANGE_NULL = "Invalid Date Change: Hotel contains a reservation, therefore null hotel dates cannot be entered.";
    public static final String INVALID_HOTEL_ID = "Invalid Hotel ID: Hotel ID can not be null";
    public static final String INVALID_HOTEL_IN_RESERVATION = "Invalid Hotel ID: The Hotel ID does not exist";
    public static final String INVALID_ID_EXISTENCE = "Invalid ID: The id entered does not exist";
    public static final String INVALID_GUESTS = "Invalid Guests: Guests must be a non-zero number";
    public static final String PARSE_ERROR = "Parsing error occurred.";
    public static final String INVALID_HOTEL_DELETE = "Invalid Request: Cannot delete hotel as there are active reservations.";
}
