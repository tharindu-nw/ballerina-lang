import ballerina/http;

function main (string[] args) {
    endpoint<http:HttpClient> httpEndpoint {
         create http:HttpClient("http://test.com", c<caret>);
    }
}
