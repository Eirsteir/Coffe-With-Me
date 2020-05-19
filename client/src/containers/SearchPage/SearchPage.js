import React from "react";
import { Redirect, withRouter } from "react-router-dom";

import Grid from "@material-ui/core/Grid";

import SearchResultsList from "../../components/Search/SearchResultsList"
import Typography from "@material-ui/core/Typography";

class SearchPage extends React.Component {

    subtractFromLengthIfUserIsIncluded = (results, userId) => {
        let length = results.length;
        results.map((user, i) => {
            if (user.id === userId) {
                length--;
            }
        });
        
        return length; 
    }

    render() {
        const { userId, results, isAuthenticated } = this.props.location.state;        
        
        if (!isAuthenticated) {
            return <Redirect to="/" />;
        }

        const amountOfResults = this.subtractFromLengthIfUserIsIncluded(results, userId);

        return (
            <div
                style={{
                        color: "#fff",
                        height: "18rem",
                        padding: "1rem",
                        paddingLeft: "2rem",
                        border: "none",
                        borderRadius: 5
                }}
            >
             <Grid
                 container
                 spacing={1}
                 direction="column"
                 alignItems="flex-start"
                 justify="flex-start"
                 style={{ minHeight: '70vh' }}
             >
                 <Grid item>
                     <Typography
                         variant="subtitle1"
                         style={{ color: "#fff", fontWeight: 300 }}
                         id="sub-header"
                     >
                         Users
                     </Typography>
                 </Grid>

                 <Grid item>
                     <p
                         style={{ color: "#D3D3D3" }}
                     >
                         {amountOfResults} user{amountOfResults === 1 ? " ": "s "}
                          found | View 50 per page
                     </p>
                 </Grid>

                 { !results.length
                     ? <div>Try another term to get more results</div>
                     : (
                         <div style={{ marginTop: "1rem" }}>

                             <SearchResultsList userId={userId} results={results} isAuthenticated={isAuthenticated} />

                         </div>
                     )}
             </Grid>
    </div>
        )
    }
}

export default withRouter(SearchPage);