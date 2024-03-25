package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ID;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PAYMENT;

import java.util.stream.Stream;

import seedu.address.logic.commands.MarkPaymentCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.person.Id;

/**
 * Parses input arguments and creates a new MarkPaymentCommand object
 */
public class MarkPaymentCommandParser implements Parser<MarkPaymentCommand> {

    @Override
    public MarkPaymentCommand parse(String args) throws ParseException {
        ArgumentMultimap argMultimap =
                ArgumentTokenizer.tokenize(args, PREFIX_ID, PREFIX_PAYMENT);

        if (!arePrefixesPresent(argMultimap, PREFIX_ID, PREFIX_PAYMENT)
                || !argMultimap.getPreamble().isEmpty()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, MarkPaymentCommand.MESSAGE_USAGE));
        }

        Id id = ParserUtil.parseId(argMultimap.getValue(PREFIX_ID).get());
        double amount = ParserUtil.parsePayment(argMultimap.getValue(PREFIX_PAYMENT).get());

        return new MarkPaymentCommand(id, amount);
    }

    private static boolean arePrefixesPresent(ArgumentMultimap argumentMultimap, Prefix... prefixes) {
        return Stream.of(prefixes).allMatch(prefix -> argumentMultimap.getValue(prefix).isPresent());
    }
}
