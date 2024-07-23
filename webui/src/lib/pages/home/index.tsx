import { Grid, Card, CardHeader, CardBody, CardFooter, Heading, Text, Button, Stack, Box } from '@chakra-ui/react';
import { useEffect, useState } from 'react';
import axios from 'axios';

const Home = () => {
  const [sandboxes, setSandboxes] = useState([]);

  useEffect(() => {
    axios.get('/api/getsandboxes')
      .then(response => {
        setSandboxes(response.data);
      })
      .catch(error => {
        console.error('There was an error fetching the data!', error);
      });
  }, []);

  return (
    <Grid gap={4}>
      {sandboxes.map((sandbox, index) => (
        <Card key={index} direction={{ base: 'column', sm: 'row' }} overflow="hidden" variant="outline">
          <Stack>
            <CardHeader>
              <Heading size="md">{sandbox.name}</Heading>
            </CardHeader>
            <CardBody>
              <Text><strong>RAM:</strong> {sandbox.ram} MB</Text>
              <Text><strong>JAR Path:</strong> {sandbox.jarPath}</Text>
              <Text><strong>Status:</strong> {sandbox.status}</Text>
              <Text><strong>Autostart:</strong> {sandbox.autostart ? 'Yes' : 'No'}</Text>
              <Text><strong>Start Time:</strong> {sandbox.startTime ? new Date(sandbox.startTime).toLocaleString() : 'N/A'}</Text>
            </CardBody>
            <CardFooter>
              <Button variant="solid" colorScheme="blue">Manage</Button>
            </CardFooter>
          </Stack>
        </Card>
      ))}
    </Grid>
  );
};

export default Home;
