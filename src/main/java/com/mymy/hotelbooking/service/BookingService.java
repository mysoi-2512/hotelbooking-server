package com.mymy.hotelbooking.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.mymy.hotelbooking.exception.InvalidBookingRequestException;
import com.mymy.hotelbooking.exception.ResourceNotFoundException;
import com.mymy.hotelbooking.model.BookedRoom;
import com.mymy.hotelbooking.model.Room;
import com.mymy.hotelbooking.repository.BookingRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BookingService implements IBookingService {
	
	private final BookingRepository bookingRepository;
	private final IRoomService roomService;

	@Override
	public List<BookedRoom> getAllBookingByRoomId(Long roomId) {
		return bookingRepository.findByRoomId(roomId);
	}

	@Override
	public List<BookedRoom> getAllBookings() {
		return bookingRepository.findAll();
	}

	@Override
	public BookedRoom findByBookingConfirmationCode(String confirmationCode) {
		return bookingRepository.findByBookingConfirmationCode(confirmationCode)
				.orElseThrow(() -> new ResourceNotFoundException("No booking found with booking code: " + confirmationCode));
	}

	@Override
	public String saveBooking(Long roomId, BookedRoom bookingRequest) {
		if(bookingRequest.getCheckOutDate().isBefore(bookingRequest.getCheckInDate())) {
			throw new InvalidBookingRequestException("Check-in date must come before check-out date");			
		}
		Room room = roomService.getRoomByRoomId(roomId).get();
		List<BookedRoom> exisBookings = room.getBookings();
		boolean roomIsAvailable = roomIsAvailable(bookingRequest, exisBookings);
		if (roomIsAvailable) {
			room.addBooking(bookingRequest);
			bookingRepository.save(bookingRequest);
		} else {
			throw new InvalidBookingRequestException("Sorry! This room is not available for the selected dates.");
		}
		return bookingRequest.getBookingConfirmationCode();		
	}
	
	@Override
	public void cancelBooking(Long bookingId) {
		bookingRepository.deleteById(bookingId);
		
	}
	
	@Override
	public List<BookedRoom> getBookingsByUserEmail(String email) {
		return bookingRepository.findByGuestEmail(email);
	}

	private boolean roomIsAvailable(BookedRoom bookingRequest, List<BookedRoom> exisBookings) {
		return exisBookings.stream()
                .noneMatch(existingBooking ->
                        bookingRequest.getCheckInDate().equals(existingBooking.getCheckInDate())
                                || bookingRequest.getCheckOutDate().isBefore(existingBooking.getCheckOutDate())
                                || (bookingRequest.getCheckInDate().isAfter(existingBooking.getCheckInDate())
                                && bookingRequest.getCheckInDate().isBefore(existingBooking.getCheckOutDate()))
                                || (bookingRequest.getCheckInDate().isBefore(existingBooking.getCheckInDate())

                                && bookingRequest.getCheckOutDate().equals(existingBooking.getCheckOutDate()))
                                || (bookingRequest.getCheckInDate().isBefore(existingBooking.getCheckInDate())

                                && bookingRequest.getCheckOutDate().isAfter(existingBooking.getCheckOutDate()))

                                || (bookingRequest.getCheckInDate().equals(existingBooking.getCheckOutDate())
                                && bookingRequest.getCheckOutDate().equals(existingBooking.getCheckInDate()))

                                || (bookingRequest.getCheckInDate().equals(existingBooking.getCheckOutDate())
                                && bookingRequest.getCheckOutDate().equals(bookingRequest.getCheckInDate()))
                );
	}
}
