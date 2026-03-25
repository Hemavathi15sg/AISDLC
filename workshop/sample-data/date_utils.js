/**
 * This is a simple utility to format dates.
 * Vague prompt scenario: "Write a function to format dates."
 */

function formatDate(date) {
    if (!date) return '';
    return new Date(date).toDateString();
}

module.exports = { formatDate };
