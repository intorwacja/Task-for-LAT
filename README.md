
# Quick Guide for LAT Task

## Endpoints requests

### GET

1. http://localhost:8080/products/{id}  
    Returns a product from database with certain ID.  

2. http://localhost:8080/products/all  
    Returns every product stored in database.

3. http://localhost:8080/products/calculate?id={id}&code={code}  
    Calculates price of the product with promocode.  

4. http://localhost:8080/promocodes/{code}  
    Returns promocode with certain code.

5. http://localhost:8080/promocodes/all  
    Returns every promocode in database.

6. http://localhost:8080/purchase/report  
    Generates report of purchases sorted by currency.  
  
### PUT
1. http://localhost:8080/products/{id}  
    Changes already existing product in database.  
    Request example:  

```

    PUT http://localhost:8080/products/1  
    Content-Type: application/json    
     
    {  
        "name": "changed name",  
        "price": "0",  
        "currency": "USD",  
        "description": "changed description"  
    }

```
### POST  

1. http://localhost:8080/products/add  
    Adds product to database.  
    Request example:  

```

    POST http://localhost:8080/products/add     
    Content-Type: application/json  
   
    {  
        "name": "example product",  
        "price": "0",  
        "currency": "EUR",  
        "description": "Product example"  
    }  
```

2. http://localhost:8080/promocodes/add  
    Adds promocode to database.
    Request example:  

```

    POST http://localhost:8080/promocodes/add  
    Content-Type: application/json  

    {  
        "code": "examplepromocode",  
        "promoCodeType": "value",  
        "usageLimit": 5,  
        "codeCurrency": "EUR",  
        "codeValue": "10",  
        "expDate": "2024-05-09T12:05:02"  
    }
```

3. http://localhost:8080/purchase/{id}?code={code}  
    Adds purchase to database and decrements promocode usage (if available).
    Request example:  

```

    POST http://localhost:8080/purchase/1?code=examplepromocode  
```