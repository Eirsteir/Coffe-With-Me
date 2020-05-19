import React from "react";
import SearchResultsItem from "./SearchResultsItem";


const SearchResultsList = (props) => {
        
    const { userId, results, isAuthenticated } = props;

    return (
        <div style={{ marginTop: "1rem" }}>
            {results.map((user, i) => {

                if (user.id === userId) {
                    return;
                }

                return (
                    <SearchResultsItem
                        key={i}
                        userId={userId}
                        user={user}
                        isAuthenticated={isAuthenticated}
                    />
                );
            })}
        </div>
    );
}

export default SearchResultsList;