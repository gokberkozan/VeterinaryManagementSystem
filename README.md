## Veterinary Management System
This project is a Spring Boot application designed to handle veterinary clinic operations.
### Summary
The project enables veterinary staff to register, update, view and delete doctors, clients, animals, vaccinations and appointments in the system.
### Entities
- Animal
- Customer
- Vaccine
- Doctor
- AvailableDate
- Appointment
## API Basic Features
### Management of Animals and Their Owners (customer)
- Register, update, view and delete animals
- Register pet owners, update, view and delete their information
- Creating an end point to filter pet owners by name.
- Creating an end point so that animals are filtered by name.
- Creating the API end point to view all the pets of the pet owner registered in the system. You should filter the animals according to the animal owner.
### Management of Applied Vaccines
- Registering, updating, viewing and deleting vaccinations applied to animals
- If the vaccine protection expiration date for the patient's same type of vaccine (vaccine with the same name and code) has not yet come, a new vaccine should not be entered into the system. You can check this from vaccination codes and vaccination expiration dates.    
- Creating the necessary API end point to list all vaccination records for a specific animal by animal id.
- To create an API end point that returns a list of vaccines with animal information whose vaccine protection expiry date is within this range, according to the start and end dates entered by the user, so that he can list the animals whose vaccine protection expiry date is approaching.
### Appointment Management
- Creating vaccination and examination appointments for animals, updating, viewing and deleting their information
- Appointments must be recorded in the system with date and time. LocalDateTime should be used for this.
- Appointments should be made with doctors on appropriate dates and times for all kinds of examinations of animals. Appointments can only be made per doctor per hour. Assume that an examination will take a fixed time of one hour.
- When creating an appointment record, it should be checked whether the doctor has an available day on the date entered or not, and if so, whether he or she has another appointment at the time entered in the appointment records. If both condition conditions are met, an appointment must be created. If the condition is not met, "The doctor is not working on this date!/Another appointment is available at the entered time." An error message like this should be thrown. You need to create a custom exception for this.
- Appointments should be filtered by user-entered date range and doctor. An API end point for this must be created. (It will be used to inquire whether the doctor and date requests of customers calling the clinic for an appointment are available.) You can look at the use of findBy between by JPA.
- Appointments should be filtered by user-entered date range and animal. An API end point for this must be created. You can look at JPA's use of findBy between.
### Veterinary Doctor Management
- Registering veterinarians, updating, viewing and deleting their information
### Management of Doctors' Available Days
- Adding doctors' available days, updating, viewing and deleting their information
- The days the doctor works will be recorded in the system as LocalDate. There will only be date information. There will be no hour, minute or second information.
### Used Technologies
- Java
- Spring Boot
- Maven
- PostgreSQL and MySQL
