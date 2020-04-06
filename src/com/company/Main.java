package com.company;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        Scanner input = new Scanner(System.in);

        System.out.println("Password: ");
        String password = input.next(); // I didn't consider white space as a character, if u want white spaces too, change into input.nextLine()
        System.out.println(solve(password));
    }

    private static int solve(String password){
        int length = password.length();
        int maximumLength = Math.min(length, 20);

        int totalChanges = 0;
        int sequences = 0;

        /*
                We keep in mind the last character @lastChar and the second to last one @secondToLast
                So we can compare it with the current character that we will find.
                If the length of the message is only 1 char, then both will have the char from position 0
                Otherwise, we assign the character from position 0 and position 1. Then we start the loop from position 2
         */
        int characterIndex = 2;
        char lastChar = password.charAt(0), secondToLastChar = password.charAt(0);
        if (length > 1) lastChar = password.charAt(1);
        int continuousSequence = 0;


        /*
                In the loop we find the continuous sequences and we delete each element from this sequence as long as
                the length is greater than 20. If its not greater than 20 we simply get the length of the sequence and I am computing
                the number of elements from this sequence that I have to change in order to escape from it.
         */
        while (characterIndex < maximumLength) {
            /// if there last 3 characters are the same we increase the counter @continuousSequence
            if (password.charAt(characterIndex) == lastChar && lastChar == secondToLastChar) continuousSequence += 1;
            else {
                secondToLastChar = lastChar;
                lastChar = password.charAt(characterIndex);
                while (length > 20 && continuousSequence > 0) {
                    /// delete the last element of the continuous sequence
                    password = password.substring(0, characterIndex - 1).concat(password.substring(characterIndex, length));
                    length = password.length();
                    continuousSequence -= 1;
                    characterIndex -= 1;
                    totalChanges += 1;
                }
                maximumLength = Math.min(length, 20);
                /// if the length is < 20 then I am computing the number of elements I have to change in order to have a good sequence
                if (continuousSequence > 0) sequences += (continuousSequence - 1) / 3 + 1;
                continuousSequence = 0;
            }
            characterIndex += 1;
        }

        /// Here I am redoing the steps from above in case there is a continuous sequence at the end of the 20 chars string
        while (length > 20 && continuousSequence > 0) {
            password = password.substring(0, characterIndex - 1).concat(password.substring(characterIndex, length));
            length = password.length();
            continuousSequence -= 1;
            characterIndex -= 1;
            totalChanges += 1;
        }
        maximumLength = Math.min(length, 20);
        if (continuousSequence > 0) sequences += (continuousSequence - 1) / 3 + 1;

        /// I verify if I have the types of special characters in my password

        boolean hasUpperCharacter = password.chars().anyMatch(Character::isUpperCase);
        boolean hasLowerCharacter = password.chars().anyMatch(Character::isLowerCase);
        boolean hasDigit = password.chars().anyMatch(Character::isDigit);

        /*
                After I computed the number of elements I need to change in order to have no continuous sequences @sequences, I can transform them
                into the special characters I don't have.  The remaining elements I will consider that they need to be changed with some other value.
         */
        while (sequences > 0) {
            totalChanges += 1;
            if (!hasDigit) hasDigit = true;
            else if (!hasLowerCharacter) hasLowerCharacter = true;
            else if (!hasUpperCharacter) hasUpperCharacter = true;
            sequences -= 1;
        }

        /*
                For length < 6.
                I put my special characters that are missing first.
                Once I am done with them I just put random characters to fill the gap.
         */

        if (length < 6) {
            if (!hasDigit) {
                totalChanges += 1;
                length += 1;
            }
            if (!hasLowerCharacter) {
                totalChanges += 1;
                length += 1;
            }
            if (!hasUpperCharacter) {
                totalChanges += 1;
                length += 1;
            }
            if (length < 6) totalChanges += 6 - length;
        }

        /*
                For length between 6 and 20.
                If my special characters are missing I will transform characters that I don't need into them.
         */
        else if (length <= 20) {
            if (!hasDigit) totalChanges += 1;
            if (!hasLowerCharacter) totalChanges += 1;
            if (!hasUpperCharacter) totalChanges += 1;
        }

        /*
                For length > 20.
                I verify if the whole password has the special characters. If so, it means that I don't have to replace anything just to
                remove elements that I don't need.
                If I don't have a special character then I consider deleting the characters that are outside my scope and then
                modifying a character into the special character that I need.
         */
        else {
            hasUpperCharacter = password.chars().anyMatch(Character::isUpperCase);
            hasLowerCharacter = password.chars().anyMatch(Character::isLowerCase);
            hasDigit = password.chars().anyMatch(Character::isDigit);
            if (!hasDigit) totalChanges += 1;
            if (!hasLowerCharacter) totalChanges += 1;
            if (!hasUpperCharacter) totalChanges += 1;
            totalChanges += length - 20;
        }

        return totalChanges;
    }
}
