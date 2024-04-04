package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static seedu.address.testutil.Assert.assertThrows;

import org.junit.jupiter.api.Test;

import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.person.Id;
import seedu.address.model.person.Person;
import seedu.address.testutil.PersonBuilder;


public class MarkPaymentCommandTest {

    private Model model = new ModelManager();

    @Test
    public void execute_paymentUpdatedSuccessfully() throws Exception {
        Person personToMarkPayment = new PersonBuilder().withPayment(60.0).build();
        model.addPerson(personToMarkPayment);
        Id idOfPerson = personToMarkPayment.getUniqueId();

        double paymentAmount = 50.0;
        MarkPaymentCommand markPaymentCommand = new MarkPaymentCommand(idOfPerson, paymentAmount);

        double expectedNewPaymentAmount = Math.max(0, personToMarkPayment.getPayment().getAmount() - paymentAmount);
        String expectedMessage = String.format(MarkPaymentCommand.MESSAGE_SUCCESS, idOfPerson, paymentAmount);

        CommandResult commandResult = markPaymentCommand.execute(model);

        assertEquals(expectedMessage, commandResult.getFeedbackToUser());
        assertEquals(expectedNewPaymentAmount,
                model.getPersonByUniqueId(idOfPerson.toString()).getPayment().getAmount());
    }

    @Test
    public void execute_personNotFound_throwsCommandException() {
        Id nonExistentId = new Id("nonExistentId");
        MarkPaymentCommand markPaymentCommand = new MarkPaymentCommand(nonExistentId, 50.0);

        assertThrows(CommandException.class,
                Messages.MESSAGE_PERSON_NOT_FOUND, () -> markPaymentCommand.execute(model));
    }

    @Test
    public void execute_paymentAlreadySettled_throwsCommandException() {
        Person personWithZeroPayment = new PersonBuilder().withPayment("0.0").build();
        model.addPerson(personWithZeroPayment);
        Id idOfPerson = personWithZeroPayment.getUniqueId();

        MarkPaymentCommand markPaymentCommand = new MarkPaymentCommand(idOfPerson, 50.0);

        // Assert that executing the command throws CommandException
        assertThrows(CommandException.class,
                "Payment is already fully settled for person with ID: "
                        + idOfPerson, () -> markPaymentCommand.execute(model));
    }

    @Test
    public void execute_amountExceedsCurrentPayment_throwsCommandException() {
        Person personWithPayment = new PersonBuilder().withPayment("50.0").build();
        model.addPerson(personWithPayment);
        Id idOfPerson = personWithPayment.getUniqueId();

        MarkPaymentCommand markPaymentCommand = new MarkPaymentCommand(idOfPerson, 100.0);

        // Assert that executing the command throws CommandException
        assertThrows(CommandException.class,
                "Amount to mark as paid exceeds the current payment due of $50.00 for person with ID: "
                        + idOfPerson, () -> markPaymentCommand.execute(model));
    }
}
