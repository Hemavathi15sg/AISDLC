import React from 'react';

/**
 * A simple Button component.
 * Vague prompt scenario: "Write tests for my component."
 */
const Button = ({ onClick, isLoading, children }) => {
    return (
        <button 
            onClick={onClick} 
            disabled={isLoading}
            style={{ opacity: isLoading ? 0.5 : 1 }}
        >
            {isLoading ? 'Loading...' : children}
        </button>
    );
};

export default Button;
