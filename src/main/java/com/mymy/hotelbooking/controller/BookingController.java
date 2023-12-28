package com.mymy.hotelbooking.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mymy.hotelbooking.exception.InvalidBookingRequestException;
import com.mymy.hotelbooking.exception.ResourceNotFoundException;
import com.mymy.hotelbooking.model.BookedRoom;
import com.mymy.hotelbooking.model.Room;
import com.mymy.hotelbooking.response.BookingResponse;
import com.mymy.hotelbooking.response.RoomResponse;
import com.mymy.hotelbooking.service.IBookingService;
import com.mymy.hotelbooking.service.IRoomService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/bookings")
public class BookingController {
	
	private final IBookingService bookingService;
	
	private final IRoomService roomService;
	
	@GetMapping("/all-bookings")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public ResponseEntity<List<BookingResponse>> getAllBookings() {
		List<BookedRoom> bookings = bookingService.getAllBookings();
		List<BookingResponse> bookingResponses = new ArrayList<>();
		for (BookedRoom booking: bookings) {
			BookingResponse bookingResponse = getBookingResponse(booking);
			bookingResponses.add(bookingResponse);
		}
		return ResponseEntity.ok(bookingResponses);	
	};

	@GetMapping("/confirmation/{confirmationCode}")
	public ResponseEntity<?> getBookingByConfirmationCode(@PathVariable String confirmationCode) {
		try {
			BookedRoom booking = bookingService.findByBookingConfirmationCode(confirmationCode);
			BookingResponse bookingResponse = getBookingResponse(booking);
			return ResponseEntity.ok(bookingResponse);
		} catch (ResourceNotFoundException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
		}		
	};
	
	@GetMapping("/user/{email}/booking")
	public ResponseEntity<List<BookingResponse>> getBookingsByUserEmail(@PathVariable String email) {
		List<BookedRoom> bookings = bookingService.getBookingsByUserEmail(email);
		List<BookingResponse> bookingResponses = new ArrayList<>();
		for (BookedRoom booking : bookings) {
			BookingResponse bookingResponse = getBookingResponse(booking);
			bookingResponses.add(bookingResponse);		
		}
		return ResponseEntity.ok(bookingResponses);
	}
	
	@PostMapping("/room/{roomId}/booking")
	public ResponseEntity<?> saveBooking(@PathVariable Long roomId,
										@RequestBody BookedRoom bookingRequest) {
		try {
			String confirmationCode = bookingService.saveBooking(roomId, bookingRequest);
			return ResponseEntity.ok("Room booked successfully! Your booking code is: " + confirmationCode);
		} catch (InvalidBookingRequestException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	};
	
	@DeleteMapping("/booking/{bookingId}/delete")
	public ResponseEntity<Void> cancelBooking(@PathVariable Long bookingId) {
		bookingService.cancelBooking(bookingId);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	};	
	
	private BookingResponse getBookingResponse(BookedRoom booking) {
		Room theRoom = roomService.getRoomByRoomId(booking.getRoom().getId()).get();
		RoomResponse roomResponse = new RoomResponse(theRoom.getId(), 
													theRoom.getRoomType(), 
													theRoom.getRoomPrice());
		return new BookingResponse(
				booking.getBookingId(), booking.getCheckInDate(),
				booking.getCheckOutDate(),booking.getGuestFullName(),
				booking.getGuestEmail(), booking.getNumOfAdults(),
				booking.getNumOfChildren(), booking.getTotalNumOfGuest(),
				booking.getBookingConfirmationCode(), roomResponse); 		
	}

}
