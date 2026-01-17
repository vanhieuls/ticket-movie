package com.example.english.Exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

@Getter
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
@AllArgsConstructor
public enum ErrorCode {
    USER_NOT_FOUND(1001,"User not found", HttpStatus.NOT_FOUND),
    USER_NAME_ALREADY_EXISTS(1002,"User name already exists", HttpStatus.BAD_REQUEST),
    USER_EMAIL_ALREADY_EXISTS(1002,"User email already exists", HttpStatus.BAD_REQUEST),
    VERIFICATION_CODE_EXPIRED(1003,"Verification code expired", HttpStatus.BAD_REQUEST),
    INVALID_VERIFICATION_CODE(1004,"Invalid verification code", HttpStatus.BAD_REQUEST),
    ACCOUNT_ALREADY_VERIFIED(1005,"Account already verified", HttpStatus.BAD_REQUEST),
    INVALID_RESET_TOKEN(1006,"Invalid reset token", HttpStatus.BAD_REQUEST),
    RESET_TOKEN_EXPIRED(1007,"Reset token expired", HttpStatus.BAD_REQUEST),
    ACCOUNT_BE_LOCKED(1008,"Account is locked", HttpStatus.FORBIDDEN),
    UNAUTHENTICATED_ACCESS(1009,"Unauthenticated access", HttpStatus.UNAUTHORIZED),
    UNAUTHORIZED_ACCESS(133,"You do not have permission to access this resource", HttpStatus.FORBIDDEN),
    ACCOUNT_NOT_VERIFY(1010,"Account not verified", HttpStatus.FORBIDDEN),
    ACCOUNT_LOCKED(1011,"Account locked", HttpStatus.FORBIDDEN),
    INVALID_CREDENTIALS(1012,"Invalid credentials", HttpStatus.UNAUTHORIZED),
    INVALID_TOKEN(1013,"Invalid token", HttpStatus.UNAUTHORIZED),
    INVALID_2FA_CODE(1014,"Invalid two-factor authentication code", HttpStatus.UNAUTHORIZED),
    INVALID_PASSWORD(1015,"Invalid password", HttpStatus.BAD_REQUEST),
    INVALID_CONFIRM_PASSWORD(1016,"Invalid confirm password", HttpStatus.BAD_REQUEST),
    DATA_VIOLATION(1290, "Data violation", HttpStatus.BAD_REQUEST),
                                                        ///////////////////////// movie /////////////////////////
    MOVIE_ALREADY_EXISTS(2001,"Movie already exists", HttpStatus.BAD_REQUEST),
    FILE_TOO_LARGE(2002,"File size exceeds the maximum limit", HttpStatus.BAD_REQUEST),
    MOVIE_NOT_FOUND(2003,"Movie not found", HttpStatus.NOT_FOUND),
    UNSUPPORTED_FILE_TYPE(2004,"Unsupported file type", HttpStatus.BAD_REQUEST),
    INVALID_DATE_RANGE(2005,"Invalid date range", HttpStatus.BAD_REQUEST),
    FILE_REQUIRED(2006,"File is required", HttpStatus.BAD_REQUEST),
    ///////Cinema and cinemaType/////////
    CINEMA_EXISTED(3001,"Cinema already exists", HttpStatus.BAD_REQUEST),
    CINEMA_TYPE_NOT_EXISTED(3002,"Cinema type does not exist", HttpStatus.BAD_REQUEST),
    CINEMA_NOT_EXISTED(3003,"Cinema does not exist", HttpStatus.BAD_REQUEST),

    ////////ScreenRoom and ScreenRoomType/////////
    SCREEN_ROOM_EXISTED(4001,"Screen room already exists", HttpStatus.BAD_REQUEST),
    SCREEN_ROOM_NOT_EXISTED(4002,"Screen room does not exist", HttpStatus.NOT_FOUND),
    SCREEN_ROOM_TYPE_EXISTED(4003,"Screen room type already exists", HttpStatus.BAD_REQUEST),
    SCREEN_ROOM_TYPE_NOT_EXISTED(4004,"Screen room type does not exist", HttpStatus.NOT_FOUND),
    ////////ShowTime/////////
    SHOW_TIME_OVERLAP(5001,"Show time overlaps with an existing show time", HttpStatus.BAD_REQUEST),
    SHOW_TIME_NOT_FOUND(5002,"Show time not found", HttpStatus.NOT_FOUND),
    INVALID_TIME_RANGE(5003,"Invalid time range", HttpStatus.BAD_REQUEST),
    ////////SeatType/////////
    SEAT_TYPE_EXISTED(6001,"Seat type already exists", HttpStatus.BAD_REQUEST),
    SEAT_TYPE_NOT_FOUND(6002,"Seat type does not found", HttpStatus.NOT_FOUND),
    SEAT_TYPE_LIST_EMPTY(6003,"Seat type list is empty", HttpStatus.NOT_FOUND),
    ////////Seat/////////
    SEAT_NOT_FOUND(7001,"Seat not found", HttpStatus.NOT_FOUND),
    ////////Special Day/////////
    EXIST_SPECIAL_DAY(8001,"Special day already exists", HttpStatus.BAD_REQUEST),
    SPECIAL_DAY_NOT_FOUND(8002,"Special day not found", HttpStatus.NOT_FOUND),
    ////////Ticket Price/////////
    TICKET_PRICE_EXISTED(9001,"Ticket price already exists", HttpStatus.BAD_REQUEST),
    TICKET_PRICE_NOT_EXISTED(9002,"Ticket price does not exist", HttpStatus.NOT_FOUND),
    SHOWTIME_NOT_FOUND(9003,"Showtime not found", HttpStatus.NOT_FOUND),
    //////////////////////////////////////////
    ORDER_CREATE_FAILED(11001,"Order creation failed", HttpStatus.INTERNAL_SERVER_ERROR),
    INVALID_PRICE(11002,"Invalid price", HttpStatus.BAD_REQUEST),
        ///invoice////
    INVOICE_NOT_EXISTED(12001,"Invoice does not exist", HttpStatus.NOT_FOUND)
    ;
    int code;
    String message;
    HttpStatusCode httpStatusCode;
//    ErrorCode(int code, String message, HttpStatusCode httpStatusCode){
//        this.code=code;
//        this.message=message;
//        this.httpStatusCode= httpStatusCode;
//    }
}
