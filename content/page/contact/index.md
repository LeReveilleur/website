---
title: "Contact"
slug: "contact"
readingTime: false
hideLastModified: true
menu:
    main: 
        weight: 10
        params:
            icon: contact
---

<form action="https://formspree.io/f/mwkdzzdg" method="POST" class="contact-form">
  <label for="contact-form-name">Nom</label>
  <input type="text" id="contact-form-name" name="name" required>
  <label for="contact-form-email">Email</label>
  <input type="email" id="contact-form-email" name="email" required>
  <label for="contact-form-message">Message</label>
  <textarea id="contact-form-message" name="message" required></textarea>
  <br/>
  <button type="submit">Envoyer</button>
</form>
