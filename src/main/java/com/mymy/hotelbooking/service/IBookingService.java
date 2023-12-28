package com.mymy.hotelbooking.service;

import java.util.List;

import com.mymy.hotelbooking.model.BookedRoom;

public interface IBookingService {

	List<BookedRoom> getAllBookingByRoomId(Long roomId);

	List<BookedRoom> getAllBookings();

	BookedRoom findByBookingConfirmationCode(String confirmationCode);

	String saveBooking(Long roomId, BookedRoom bookingRequest);

	void cancelBooking(Long bookingId);

	List<BookedRoom> getBookingsByUserEmail(String email);
}
