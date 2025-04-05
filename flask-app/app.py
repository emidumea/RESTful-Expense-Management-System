from flask import Flask, render_template, request, redirect, url_for, session, flash, jsonify
import requests

app = Flask(__name__)
app.secret_key = "super_secret_key"

SPRING_API_URL = "http://localhost:8080"


@app.route('/')
def home():
    return redirect(url_for('login'))


@app.route('/login', methods=['GET', 'POST'])
def login():
    if request.method == 'POST':
        username = request.form['username']
        password = request.form['password']

        response = requests.post(f"{SPRING_API_URL}/login", data={'username': username, 'password': password})

        if response.status_code == 200:
            data = response.json()
            session['user_id'] = data['user_id']
            session['username'] = username
            flash("Login successful!", "success")
            return redirect(url_for('dashboard'))
        else:
            flash("Invalid credentials, please try again.", "error")

    return render_template('login.html')

@app.route('/register', methods=['GET', 'POST'])
def register():
    if request.method == 'POST':
        username = request.form['username']
        password = request.form['password']
        name = request.form['name']

        response = requests.post(f"{SPRING_API_URL}/register", data={'username': username, 'password': password, 'name': name})

        if response.status_code == 201:
            message = "Registration successful. Please log in."
            return render_template('login.html', message=message)
        else:
            message = "User already exists. Please choose a different username."
            return render_template('register.html', message=message)

    return render_template('register.html')

@app.route('/dashboard')
def dashboard():
    if 'user_id' not in session:
        return redirect(url_for('login'))

    user_id = session['user_id']
    response = requests.get(f"{SPRING_API_URL}/expenses/{user_id}")

    if response.status_code == 200:
        expenses = response.json()
    else:
        expenses = []

    return render_template('dashboard.html', username=session['username'], expenses=expenses)


@app.route('/add_expense', methods=['GET', 'POST'])
def add_expense():
    if 'user_id' not in session:
        return redirect(url_for('login'))

    if request.method == 'POST':
        user_id = session['user_id']
        amount = request.form['amount']
        category = request.form['category']
        description = request.form['description']

        response = requests.post(f"{SPRING_API_URL}/expenses/add", data={
            'userId': user_id,
            'amount': amount,
            'category': category,
            'description': description
        })

        if response.status_code == 201:
            flash("Expense added successfully!", "success")
            return redirect(url_for('dashboard'))
        else:
            flash("Failed to add expense!", "error")

    return render_template('add_expense.html')


@app.route('/delete_expense/<int:id>')
def delete_expense(id):
    if 'user_id' not in session:
        return redirect(url_for('login'))

    response = requests.delete(f"{SPRING_API_URL}/expenses/delete/{id}")

    if response.status_code == 204:
        flash("Expense deleted successfully!", "success")
    else:
        flash("Failed to delete expense!", "error")

    return redirect(url_for('dashboard'))

@app.route('/edit_expense/<int:id>', methods=['GET'])
def edit_expense(id):
    if 'user_id' not in session:
        return redirect(url_for('login'))

    response = requests.get(f"{SPRING_API_URL}/expenses/{session['user_id']}")

    if response.status_code == 200:
        expenses = response.json()
        expense = next((exp for exp in expenses if exp['id'] == id), None)

        if expense:
            return render_template('edit_expense.html', expense=expense)

    flash("Expense not found!", "error")
    return redirect(url_for('dashboard'))

@app.route('/update_expense/<int:id>', methods=['POST'])
def update_expense(id):
    if 'user_id' not in session:
        return jsonify({"success": False, "message": "Unauthorized"}), 401

    amount = request.form['amount']
    category = request.form['category']
    description = request.form['description']

    response = requests.put(f"{SPRING_API_URL}/expenses/update/{id}", json={
        'amount': float(amount),
        'category': category,
        'description': description
    })

    if response.status_code == 200:
        return jsonify({"success": True})
    else:
        return jsonify({"success": False, "message": "Update failed"}), 400


@app.route('/logout')
def logout():
    session.clear()
    flash("You have been logged out.", "info")
    return redirect(url_for('login'))


if __name__ == '__main__':
    app.run(debug=True)
